package domain

// TODO: la API HTTP del servicio de tu dominio. Copia este archivo al paquete
// del servicio de la semana 33 y cambia la línea package (paso 3).
//
//   - NewHandler(s *TuServicio) http.Handler con un http.ServeMux y patrones
//     con método: "GET /api/pieces/{code}", "POST /api/pieces/{code}/loan".
//   - Las mismas rutas y los mismos códigos de estado que tu API Java de la
//     semana 28: cada error del dominio, con errors.Is, a su código.
//   - Pasa r.Context() al servicio.
