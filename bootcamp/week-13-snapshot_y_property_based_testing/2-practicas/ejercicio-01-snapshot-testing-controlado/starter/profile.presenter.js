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

// Respuesta con un campo volátil: generatedAt cambia en cada ejecución.
function buildProfileResponse(user) {
  return {
    profile: buildPublicProfile(user),
    generatedAt: new Date().toISOString(),
  };
}

module.exports = {
  buildPublicProfile,
  buildPublicProfileList,
  buildProfileResponse,
};
