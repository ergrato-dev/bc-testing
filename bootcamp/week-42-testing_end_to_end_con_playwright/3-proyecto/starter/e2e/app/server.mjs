// La aplicación bajo prueba: un catálogo web sin dependencias, con los datos en memoria.
// Todo lo que cambia de un dominio a otro está en domain.mjs; este archivo no se toca.
import { createServer } from 'node:http';
import { domain } from './domain.mjs';

const port = Number(process.env.PORT ?? 3000);
// Guardar y ejecutar una acción tardan un poco, como en una aplicación con base de datos.
const delayMs = Number(process.env.SAVE_DELAY_MS ?? 400);

const items = domain.seed.map((item) => ({ ...item, status: item.status ?? domain.initialStatus }));
const keyField = domain.fields[0].name;

const esc = (value) =>
  String(value ?? '').replace(/[&<>"']/g, (c) => `&#${c.charCodeAt(0)};`);
const sleep = (ms) => new Promise((resolve) => setTimeout(resolve, ms));
const find = (key) => items.find((item) => item[keyField] === key);

function validate(input) {
  const errors = [];
  const item = { status: domain.initialStatus };
  for (const field of domain.fields) {
    const raw = input[field.name];
    if (field.type === 'checkbox') {
      item[field.name] = raw === true || raw === 'on';
      continue;
    }
    const text = String(raw ?? '').trim();
    if (text === '') {
      if (field.required) errors.push(`${field.label} es obligatorio`);
      item[field.name] = field.type === 'number' ? null : '';
      continue;
    }
    if (field.type === 'number') {
      const number = Number(text);
      if (!Number.isInteger(number)) errors.push(`${field.label} debe ser un número entero`);
      else if (field.min !== undefined && number < field.min) errors.push(`${field.label} debe ser al menos ${field.min}`);
      else if (field.max !== undefined && number > field.max) errors.push(`${field.label} debe ser como mucho ${field.max}`);
      item[field.name] = number;
      continue;
    }
    if (field.type === 'select' && !field.options.includes(text)) errors.push(`${field.label} no es válido`);
    if (field.pattern && !new RegExp(field.pattern).test(text)) errors.push(field.patternMessage);
    item[field.name] = text;
  }
  if (item[keyField] && find(item[keyField])) errors.push(`${domain.fields[0].label} ya existe`);
  return { item, errors };
}

function page(title, body) {
  return `<!doctype html>
<html lang="es">
<head>
<meta charset="utf-8">
<title>${esc(title)} - ${esc(domain.app)}</title>
<style>
  body { font-family: sans-serif; margin: 2rem; max-width: 60rem; }
  table { border-collapse: collapse; width: 100%; }
  th, td { border-bottom: 1px solid #ccc; padding: .4rem; text-align: left; }
  .status { position: relative; }
  .status [role=tooltip] { display: none; position: absolute; left: 0; top: 1.5rem; background: #333; color: #fff; padding: .3rem; z-index: 1; width: 14rem; }
  .status:hover [role=tooltip] { display: block; }
  [role=alert] { color: #a00; }
  [role=status] { color: #070; }
  label { display: block; margin-top: .6rem; }
</style>
</head>
<body>
<header><a href="/">${esc(domain.app)}</a></header>
<main>
${body}
</main>
</body>
</html>`;
}

function listPage(query) {
  const q = (query.get('q') ?? '').toLowerCase();
  const onlyAvailable = query.get('available') === 'on';
  const shown = items.filter(
    (item) =>
      domain.fields.some((f) => String(item[f.name] ?? '').toLowerCase().includes(q)) &&
      (!onlyAvailable || item.status === domain.initialStatus),
  );
  const created = query.get('created');
  const rows = shown
    .map(
      (item) => `<tr>
  <td><a href="/items/${encodeURIComponent(item[keyField])}">${esc(item[keyField])}</a></td>
  ${domain.listFields.map((name) => `<td>${esc(item[name])}</td>`).join('')}
  <td class="status"><span>${esc(item.status)}</span><span role="tooltip">${esc(domain.statusHelp[item.status])}</span></td>
</tr>`,
    )
    .join('\n');
  const labelOf = (name) => domain.fields.find((f) => f.name === name).label;
  return page(
    domain.plural,
    `<h1>${esc(domain.plural)}</h1>
${created ? `<p role="status">${esc(created)} guardado</p>` : ''}
<form method="get" action="/" role="search">
  <label>Buscar <input type="search" name="q" value="${esc(query.get('q'))}"></label>
  <label><input type="checkbox" name="available"${onlyAvailable ? ' checked' : ''}> ${esc(domain.filterLabel)}</label>
  <button>Buscar</button>
</form>
<p><a href="/new">${esc(domain.newLabel)}</a></p>
<table>
<thead><tr><th>${esc(domain.fields[0].label)}</th>${domain.listFields.map((n) => `<th>${esc(labelOf(n))}</th>`).join('')}<th>Estado</th></tr></thead>
<tbody>
${rows}
</tbody>
</table>
<p data-testid="count">${shown.length} de ${items.length}</p>`,
  );
}

function input(field, value) {
  const id = `f-${field.name}`;
  if (field.type === 'checkbox') {
    return `<label><input type="checkbox" id="${id}" name="${field.name}"${value ? ' checked' : ''}> ${esc(field.label)}</label>`;
  }
  if (field.type === 'select') {
    const options = field.options
      .map((o) => `<option${o === value ? ' selected' : ''}>${esc(o)}</option>`)
      .join('');
    return `<label for="${id}">${esc(field.label)}</label><select id="${id}" name="${field.name}"><option value="">Elige una opción</option>${options}</select>`;
  }
  const type = field.type === 'number' ? 'number' : 'text';
  return `<label for="${id}">${esc(field.label)}</label><input type="${type}" id="${id}" name="${field.name}" value="${esc(value)}">`;
}

function formPage(values = {}, errors = []) {
  const alert = errors.length
    ? `<div role="alert"><ul>${errors.map((e) => `<li>${esc(e)}</li>`).join('')}</ul></div>`
    : '';
  return page(
    domain.newLabel,
    `<h1>${esc(domain.newLabel)}</h1>
${alert}
<form method="post" action="/new" novalidate>
${domain.fields.map((f) => input(f, values[f.name])).join('\n')}
<p><button>Guardar</button></p>
</form>`,
  );
}

function detailPage(item, message) {
  const buttons = domain.actions
    .map(
      (action, i) =>
        `<form method="post" action="/items/${encodeURIComponent(item[keyField])}/actions/${i}"><button${action.allowed(item) ? '' : ' disabled'}>${esc(action.label)}</button></form>`,
    )
    .join('\n');
  return page(
    item[keyField],
    `<h1>${esc(item[domain.titleField])}</h1>
${message ? `<p role="status">${esc(message)}</p>` : ''}
<dl>
${domain.fields.map((f) => `<dt>${esc(f.label)}</dt><dd>${f.type === 'checkbox' ? (item[f.name] ? 'Sí' : 'No') : esc(item[f.name])}</dd>`).join('\n')}
<dt>Estado</dt><dd data-testid="status">${esc(item.status)}</dd>
</dl>
${buttons}
<p><a href="/">Volver a ${esc(domain.plural.toLowerCase())}</a></p>
<script>
  // Las acciones se envían con fetch y la página se actualiza sin recargarse.
  for (const form of document.querySelectorAll('form')) {
    form.addEventListener('submit', async (event) => {
      event.preventDefault();
      const response = await fetch(form.action, { method: 'POST', headers: { accept: 'application/json' } });
      const result = await response.json();
      document.querySelector('[data-testid=status]').textContent = result.status;
      document.querySelectorAll('form button').forEach((b, i) => { b.disabled = !result.allowed[i]; });
      let message = document.querySelector('[role=status]');
      if (!message) {
        message = document.createElement('p');
        message.setAttribute('role', 'status');
        document.querySelector('h1').after(message);
      }
      message.textContent = result.message;
    });
  }
</script>`,
  );
}

async function body(req) {
  let text = '';
  for await (const chunk of req) text += chunk;
  if ((req.headers['content-type'] ?? '').startsWith('application/json')) return JSON.parse(text || '{}');
  return Object.fromEntries(new URLSearchParams(text));
}

function send(res, status, content, type = 'text/html; charset=utf-8') {
  res.writeHead(status, { 'content-type': type });
  res.end(content);
}

function redirect(res, location) {
  res.writeHead(303, { location });
  res.end();
}

createServer(async (req, res) => {
  const url = new URL(req.url, `http://${req.headers.host}`);
  const path = decodeURIComponent(url.pathname);
  const detail = path.match(/^\/items\/([^/]+)$/);
  const action = path.match(/^\/items\/([^/]+)\/actions\/(\d+)$/);

  if (req.method === 'GET' && path === '/') return send(res, 200, listPage(url.searchParams));
  if (req.method === 'GET' && path === '/new') return send(res, 200, formPage());
  if (req.method === 'POST' && path === '/new') {
    const values = await body(req);
    const { item, errors } = validate(values);
    if (errors.length) return send(res, 422, formPage(values, errors));
    await sleep(delayMs);
    items.push(item);
    return redirect(res, `/?created=${encodeURIComponent(item[keyField])}`);
  }
  if (req.method === 'GET' && detail && find(detail[1])) {
    return send(res, 200, detailPage(find(detail[1]), url.searchParams.get('done')));
  }
  if (req.method === 'POST' && action && find(action[1]) && domain.actions[action[2]]) {
    const item = find(action[1]);
    const chosen = domain.actions[action[2]];
    const json = (req.headers.accept ?? '').includes('application/json');
    const result = (message) =>
      JSON.stringify({ status: item.status, message, allowed: domain.actions.map((a) => a.allowed(item)) });
    if (!chosen.allowed(item)) {
      return json ? send(res, 409, result('No se puede hacer ahora'), 'application/json') : send(res, 409, detailPage(item, 'No se puede hacer ahora'));
    }
    await sleep(delayMs);
    chosen.apply(item);
    if (json) return send(res, 200, result(chosen.done), 'application/json');
    return redirect(res, `/items/${encodeURIComponent(item[keyField])}?done=${encodeURIComponent(chosen.done)}`);
  }
  // La API JSON sirve para preparar datos desde los tests sin pasar por la interfaz.
  if (req.method === 'GET' && path === '/api/items') return send(res, 200, JSON.stringify(items), 'application/json');
  if (req.method === 'POST' && path === '/api/items') {
    const { item, errors } = validate(await body(req));
    if (errors.length) return send(res, 422, JSON.stringify({ errors }), 'application/json');
    items.push(item);
    return send(res, 201, JSON.stringify(item), 'application/json');
  }
  send(res, 404, page('No encontrado', '<h1>No encontrado</h1>'));
}).listen(port, () => console.log(`${domain.app} en http://localhost:${port}`));
