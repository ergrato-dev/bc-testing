const { getUserById } = require("./user.repository");

test("should return user when id is valid", async () => {
  const result = await getUserById(1);
  expect(result).toMatchObject({ id: 1 });
});

test("should resolve with user payload when id is valid", async () => {
  await expect(getUserById(2)).resolves.toMatchObject({ id: 2 });
});

test("should reject when id is invalid", async () => {
  await expect(getUserById(0)).rejects.toThrow("ValidationError");
});
