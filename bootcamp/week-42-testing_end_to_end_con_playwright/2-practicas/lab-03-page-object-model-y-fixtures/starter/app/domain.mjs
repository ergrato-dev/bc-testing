// El dominio de la aplicación: qué se guarda, cómo se valida y qué acciones tiene.
export const domain = {
  app: 'Biblioteca municipal',
  newLabel: 'Nuevo libro',
  plural: 'Libros',
  // El primer campo es la clave: única, y forma parte de la URL de cada libro.
  fields: [
    { name: 'isbn', label: 'ISBN', type: 'text', required: true, pattern: '^[0-9]{13}$', patternMessage: 'El ISBN tiene 13 cifras' },
    { name: 'title', label: 'Título', type: 'text', required: true },
    { name: 'genre', label: 'Género', type: 'select', required: true, options: ['Novela', 'Ensayo', 'Poesía'] },
    { name: 'pages', label: 'Páginas', type: 'number', min: 1, max: 5000 },
    { name: 'reference', label: 'Solo consulta en sala', type: 'checkbox' },
  ],
  titleField: 'title',
  listFields: ['title', 'genre'],
  initialStatus: 'Disponible',
  filterLabel: 'Solo disponibles',
  statusHelp: {
    Disponible: 'En la estantería: se puede prestar',
    Prestado: 'Fuera de la biblioteca hasta su devolución',
  },
  actions: [
    {
      label: 'Prestar',
      allowed: (book) => book.status === 'Disponible' && !book.reference,
      apply: (book) => { book.status = 'Prestado'; },
      done: 'Libro prestado',
    },
    {
      label: 'Devolver',
      allowed: (book) => book.status === 'Prestado',
      apply: (book) => { book.status = 'Disponible'; },
      done: 'Libro devuelto',
    },
  ],
  seed: [
    { isbn: '9788437604947', title: 'Cien años de soledad', genre: 'Novela', pages: 471, reference: false },
    { isbn: '9788420412146', title: 'Rayuela', genre: 'Novela', pages: 736, reference: false },
    { isbn: '9788497592208', title: 'Veinte poemas de amor', genre: 'Poesía', pages: 104, reference: false, status: 'Prestado' },
    { isbn: '9788491050957', title: 'Diccionario de la lengua', genre: 'Ensayo', pages: 2400, reference: true },
  ],
};
