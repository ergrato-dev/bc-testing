const {
  buildPublicProfile,
  buildPublicProfileList,
  buildProfileResponse,
} = require("./profile.presenter");

// ============================================
// PASO 1: Caso base estable
// ============================================
// test("should build a public profile payload", () => {
//   const profile = buildPublicProfile({
//     id: "u-1",
//     firstName: "Ada",
//     lastName: "Lovelace",
//     role: "mentor",
//     isActive: true,
//   });
//
//   expect(profile).toEqual({
//     id: "u-1",
//     displayName: "Ada Lovelace",
//     role: "mentor",
//     isActive: true,
//   });
// });

// ============================================
// PASO 2: Snapshot individual
// ============================================
// test("should match snapshot for public profile", () => {
//   const profile = buildPublicProfile({
//     id: "u-1",
//     firstName: "Ada",
//     lastName: "Lovelace",
//     role: "mentor",
//     isActive: true,
//   });
//
//   expect(profile).toMatchSnapshot();
// });

// ============================================
// PASO 3: Snapshot de lista
// ============================================
// test("should match snapshot for profile list", () => {
//   const list = buildPublicProfileList([
//     { id: "u-1", firstName: "Ada", lastName: "Lovelace", role: "mentor", isActive: true },
//     { id: "u-2", firstName: "Alan", lastName: "Turing", role: "student", isActive: false },
//   ]);
//
//   expect(list).toMatchSnapshot();
// });

// ============================================
// PASO 4: Property matcher para campo volátil
// ============================================
// generatedAt cambia en cada ejecución: se valida su tipo, no su valor.
// test("should match snapshot for profile response ignoring generatedAt", () => {
//   const response = buildProfileResponse({
//     id: "u-1",
//     firstName: "Ada",
//     lastName: "Lovelace",
//     role: "mentor",
//     isActive: true,
//   });
//
//   expect(response).toMatchSnapshot({
//     generatedAt: expect.any(String),
//   });
// });

// ============================================
// PASO 5: Inline snapshot
// ============================================
// Al ejecutar sin CI, Jest escribe el snapshot dentro de los paréntesis.
// test("should match inline snapshot for public profile", () => {
//   const profile = buildPublicProfile({
//     id: "u-3",
//     firstName: "Grace",
//     lastName: "Hopper",
//     role: "mentor",
//     isActive: true,
//   });
//
//   expect(profile).toMatchInlineSnapshot();
// });
