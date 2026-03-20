const {
  buildPublicProfile,
  buildPublicProfileList,
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
