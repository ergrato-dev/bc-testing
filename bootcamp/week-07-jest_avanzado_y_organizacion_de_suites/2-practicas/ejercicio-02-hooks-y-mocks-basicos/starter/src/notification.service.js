function sendWelcomeEmail(user, gateway, logger) {
  if (!user || !user.email) {
    throw new Error("ValidationError");
  }

  const response = gateway.send(user.email, "welcome");
  logger.info("email_sent", { email: user.email });
  return response;
}

module.exports = { sendWelcomeEmail };
