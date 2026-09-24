const { getUserById, getUserProfile } = require("./user.repository");

// ============================================
// PASO 1: Async/Await básico
// ============================================
// test("should return user when id is valid", async () => {
//   const result = await getUserById(1);
//   expect(result).toMatchObject({ id: 1 });
// });

// ============================================
// PASO 2: resolves helper
// ============================================
// test("should resolve with user payload when id is valid", async () => {
//   await expect(getUserById(2)).resolves.toMatchObject({ id: 2 });
// });

// ============================================
// PASO 3: rejects helper
// ============================================
// test("should reject when id is invalid", async () => {
//   await expect(getUserById(0)).rejects.toThrow("ValidationError");
// });

// ============================================
// PASO 4: Falso positivo, expect.assertions y return
// ============================================
// // Falso positivo (NO usar): si la promesa no rechaza, el catch nunca corre,
// // no se ejecuta ningún expect y el test queda en verde sin comprobar nada.
// // test("should reject when id is invalid", async () => {
// //   try {
// //     await getUserById(5);
// //   } catch (error) {
// //     expect(error.message).toBe("ValidationError");
// //   }
// // });
// test("should reject with ValidationError when id is zero", async () => {
//   expect.assertions(1);
//   try {
//     await getUserById(0);
//   } catch (error) {
//     expect(error.message).toBe("ValidationError");
//   }
// });
//
// test("should resolve user name when the promise is returned", () => {
//   // Sin `return` Jest no espera la promesa: el test termina antes del expect.
//   return getUserById(3).then((user) => {
//     expect(user.name).toBe("Ada");
//   });
// });

// ============================================
// PASO 5: Dependencia que rechaza con mockRejectedValue
// ============================================
// test("should return profile when api resolves", async () => {
//   const profileApi = { fetchProfile: jest.fn().mockResolvedValue({ id: 1, bio: "QA" }) };
//
//   await expect(getUserProfile(1, profileApi)).resolves.toEqual({ id: 1, bio: "QA" });
//   expect(profileApi.fetchProfile).toHaveBeenCalledWith(1);
// });
//
// test("should throw ProfileUnavailable when api rejects", async () => {
//   const profileApi = { fetchProfile: jest.fn().mockRejectedValue(new Error("timeout")) };
//
//   await expect(getUserProfile(1, profileApi)).rejects.toThrow("ProfileUnavailable");
// });
