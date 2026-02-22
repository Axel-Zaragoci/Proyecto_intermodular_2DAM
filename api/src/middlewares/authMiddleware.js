import { jwtVerify } from "jose";


/** * Middleware para verificar el token JWT
 * @function verifyToken
 * @async
 * @description
 * Verifica el token JWT en el encabezado de autorización
 * Si el token es válido, agrega la información del usuario a la solicitud y llama a next() que permite continuar con la siguiente función middleware o controlador
 * Si el token es inválido o falta, devuelve un error 401
 * Utilizamos el type porque si no, TypeScript no reconoce que estamos agregando la propiedad user al objeto req y da error de TS
 * payload es la información que hemos incluido en el token al momento de crearlo (id, rol, vipStatus)
 * 
 * @param {import('express').Request} req 
 * @param {import('express').Response} res 
 * @param {import('express').NextFunction} next 
 * @returns 
 */
export async function verifyToken(req, res, next) {
  try {
    const authHeader = req.headers.authorization;
    if (!authHeader) return res.status(401).json({ error: "Token requerido" });

    const [scheme, token] = authHeader.split(" ");
    if (scheme !== "Bearer" || !token) return res.status(401).json({ error: "Formato de token inválido" });

    const encoder = new TextEncoder();
    const secret = encoder.encode(process.env.JWT_SECRET);

    const { payload } = await jwtVerify(token, secret);

    /** @type {import('express').Request & { user?: any }} */ 
    const r = (req);
    r.user = payload;

    return next();
  } catch (err) {
    return res.status(401).json({ error: "Token inválido o expirado" });
  }
}

/**
 * Middleware para autorizar roles específicos
 * 
 * @function authorizeRoles
 * 
 * @description
 * Autoriza el acceso a rutas específicas según los roles proporcionados
 * Si el rol del usuario no está en la lista de roles permitidos, devuelve un error 403
 * Si el usuario no está autenticado, devuelve un error 401
 * @param  {string[]} roles 
 * @returns http response con:
  * - Código de estado 403 y mensaje de error si el rol del usuario no está autorizado
  * - Código de estado 401 y mensaje de error si el usuario no está autenticado
  * - Llama a next() para continuar si el rol está autorizado y entra en la ruta protegida
 */
export function authorizeRoles(roles) {
  return (req, res, next) => {
    /** @type {import('express').Request & { user?: any }} */
    const r = (req);

    if (!r.user.rol) return res.status(401).json({ error: "No autenticado" });

    if (!roles.includes(r.user.rol)) return res.status(403).json({ error: "Acceso denegado" });

    return next();
  };
}
