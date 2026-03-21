function buildPublicProfile(user) {
  return {
    id: user.id,
    displayName: `${user.firstName} ${user.lastName}`.trim(),
    role: user.role,
    isActive: user.isActive,
  };
}

function buildPublicProfileList(users) {
  return users.map(buildPublicProfile);
}

module.exports = { buildPublicProfile, buildPublicProfileList };
