// TODO: cambia los nombres por los de tu servicio. Cada it lleva encima el comentario con su ID
// de la matriz, en una línea sola, como en la semana 6.
const { createService } = require("../src/service");

describe("createService", () => {
  let clock;
  let notifier;
  let service;

  beforeEach(() => {
    // Stub: para el servicio, hoy es siempre esta fecha. Elige una y calcula tus casos a partir de ella.
    clock = { today: jest.fn().mockReturnValue("2026-03-15") };
    // Mock: anota cada aviso para comprobarlo.
    notifier = { send: jest.fn() };
    service = createService({ clock, notifier });
  });

  describe("create", () => {
    describe("when the data is valid", () => {
      it.todo("should store it");
      it.todo("should send one notification with the recipient and the message");
    });

    describe("when the data breaks a rule", () => {
      it.todo("should throw and not send anything");
    });

    describe("when the date is today", () => {
      it.todo("should throw and not send anything");
    });
  });

  describe("cancel", () => {
    describe("when it exists", () => {
      it.todo("should remove it and send a notification");
    });

    describe("when it does not exist", () => {
      it.todo("should throw");
    });
  });
});
