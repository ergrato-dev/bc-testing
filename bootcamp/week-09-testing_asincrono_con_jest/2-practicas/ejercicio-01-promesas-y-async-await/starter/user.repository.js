async function getUserById(id) {
  if (id <= 0) {
    throw new Error("ValidationError");
  }

  return Promise.resolve({ id, name: "Ada" });
}

async function getUserProfile(id, profileApi) {
  try {
    return await profileApi.fetchProfile(id);
  } catch {
    throw new Error("ProfileUnavailable");
  }
}

module.exports = { getUserById, getUserProfile };
