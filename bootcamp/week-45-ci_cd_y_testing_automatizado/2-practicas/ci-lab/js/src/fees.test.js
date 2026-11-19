const { lateFee } = require('./fees');

describe('lateFee', () => {
  test.each([
    [-3, 0],
    [0, 0],
    [1, 500],
    [7, 3500],
    [20, 10000],
    [365, 10000],
  ])('%i days late costs %i', (days, expected) => {
    expect(lateFee(days)).toBe(expected);
  });

  test('rejects a non-integer number of days', () => {
    expect(() => lateFee(1.5)).toThrow(TypeError);
  });
});
