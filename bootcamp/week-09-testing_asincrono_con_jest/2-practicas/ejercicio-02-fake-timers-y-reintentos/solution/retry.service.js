function retryOperation(operation, retries = 2, delay = 1000) {
  return new Promise((resolve, reject) => {
    let attempts = 0;

    const execute = () => {
      operation()
        .then(resolve)
        .catch((error) => {
          if (attempts >= retries) {
            reject(error);
            return;
          }

          attempts += 1;
          setTimeout(execute, delay);
        });
    };

    execute();
  });
}

module.exports = { retryOperation };
