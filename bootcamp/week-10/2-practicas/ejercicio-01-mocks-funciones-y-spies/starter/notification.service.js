async function notifyUser(user, notifier) {
  if (!user?.email) {
    throw new Error("email is required");
  }

  await notifier.send({
    to: user.email,
    template: "welcome",
  });

  return { delivered: true };
}

const auditLogger = {
  log(action, payload) {
    return `${action}:${payload}`;
  },
};

module.exports = { notifyUser, auditLogger };
