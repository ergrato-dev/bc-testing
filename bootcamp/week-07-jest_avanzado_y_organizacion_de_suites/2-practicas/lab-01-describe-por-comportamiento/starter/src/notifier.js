// El aviso por correo. En producción llamaría al servidor de correo; aquí lo escribe en la consola.
const emailNotifier = {
  send(to, message) {
    console.log(`Correo a ${to}: ${message}`);
  },
};

module.exports = { emailNotifier };
