const { retryOperation } = require("./retry.service");
const { debounce } = require("./debounce");

// ============================================
// PASO 1: Activar fake timers
// ============================================
// beforeEach(() => {
//   jest.useFakeTimers();
// });
//
// afterEach(() => {
//   jest.useRealTimers();
//   jest.clearAllMocks();
// });

// ============================================
// PASO 2: Caso exitoso tras reintento (advanceTimersByTimeAsync)
// ============================================
// test("should resolve after one retry", async () => {
//   const operation = jest
//     .fn()
//     .mockRejectedValueOnce(new Error("temporary"))
//     .mockResolvedValueOnce("ok");
//
//   const promise = retryOperation(operation, 2, 1000);
//
//   // La version Async avanza el reloj y ademas drena las promesas pendientes
//   // (.then/.catch) entre timers; advanceTimersByTime sincrono no lo hace.
//   await jest.advanceTimersByTimeAsync(1000);
//
//   await expect(promise).resolves.toBe("ok");
//   expect(operation).toHaveBeenCalledTimes(2);
// });

// ============================================
// PASO 3: Caso de fallo final
// ============================================
// test("should reject when retries are exhausted", async () => {
//   const operation = jest.fn().mockRejectedValue(new Error("fatal"));
//
//   const promise = retryOperation(operation, 1, 1000);
//   // Se engancha el expect antes de avanzar el reloj para que el rechazo no quede sin manejar.
//   const assertion = expect(promise).rejects.toThrow("fatal");
//
//   await jest.advanceTimersByTimeAsync(1000);
//
//   await assertion;
//   expect(operation).toHaveBeenCalledTimes(2);
// });

// ============================================
// PASO 4: Debounce con fake timers
// ============================================
// test("should call search only once with the last term after rapid typing", () => {
//   const search = jest.fn();
//   const debouncedSearch = debounce(search, 300);
//
//   debouncedSearch("pla");
//   jest.advanceTimersByTime(299);
//   debouncedSearch("planeta");
//   jest.advanceTimersByTime(299);
//
//   expect(search).not.toHaveBeenCalled();
//
//   jest.advanceTimersByTime(1);
//
//   expect(search).toHaveBeenCalledTimes(1);
//   expect(search).toHaveBeenCalledWith("planeta");
// });
