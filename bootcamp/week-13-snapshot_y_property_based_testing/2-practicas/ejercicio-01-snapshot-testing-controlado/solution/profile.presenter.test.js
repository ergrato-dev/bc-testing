const {
  buildPublicProfile,
  buildPublicProfileList,
  buildProfileResponse,
} = require("./profile.presenter");

test("should build a public profile payload", () => {
  const profile = buildPublicProfile({
    id: "u-1",
    firstName: "Ada",
    lastName: "Lovelace",
    role: "mentor",
    isActive: true,
  });

  expect(profile).toEqual({
    id: "u-1",
    displayName: "Ada Lovelace",
    role: "mentor",
    isActive: true,
  });
});

test("should match snapshot for public profile", () => {
  const profile = buildPublicProfile({
    id: "u-1",
    firstName: "Ada",
    lastName: "Lovelace",
    role: "mentor",
    isActive: true,
  });

  expect(profile).toMatchSnapshot();
});

test("should match snapshot for profile list", () => {
  const list = buildPublicProfileList([
    {
      id: "u-1",
      firstName: "Ada",
      lastName: "Lovelace",
      role: "mentor",
      isActive: true,
    },
    {
      id: "u-2",
      firstName: "Alan",
      lastName: "Turing",
      role: "student",
      isActive: false,
    },
  ]);

  expect(list).toMatchSnapshot();
});

test("should match snapshot for profile response ignoring generatedAt", () => {
  const response = buildProfileResponse({
    id: "u-1",
    firstName: "Ada",
    lastName: "Lovelace",
    role: "mentor",
    isActive: true,
  });

  expect(response).toMatchSnapshot({
    generatedAt: expect.any(String),
  });
});

test("should match inline snapshot for public profile", () => {
  const profile = buildPublicProfile({
    id: "u-3",
    firstName: "Grace",
    lastName: "Hopper",
    role: "mentor",
    isActive: true,
  });

  expect(profile).toMatchInlineSnapshot(`
{
  "displayName": "Grace Hopper",
  "id": "u-3",
  "isActive": true,
  "role": "mentor",
}
`);
});
