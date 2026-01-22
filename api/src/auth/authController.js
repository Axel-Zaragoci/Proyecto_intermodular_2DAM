import { SignJWT } from 'jose';
import { comparePassword } from "../services/password.service.js";
import { userDatabaseModel } from '../users/usersModel.js';
import dotenv from 'dotenv';
dotenv.config();

/**
 * Login de usuario
 * @function login
 * @async
 * 
 * @description
 * Recibe dni y password desde el cuerpo de la solicitud
 * Verifica si el usuario existe y si la contraseña es correcta
 * Genera un token JWT si las credenciales son válidas
 * Devuelve el token y la información del usuario o un mensaje de error si las credenciales son inválidas
 * 
 * @param {import('express').Request} req 
 * @param {import('express').Response} res 
 * @returns 
 */
export async function login(req, res) {
  try {
    const { dni, password } = req.body;

    if (!dni || !password) {
      return res.status(400).json({ error: "DNI y password son obligatorios" });
    }

    const user = await userDatabaseModel.findOne({ dni }).select("+password");
    if (!user) return res.status(401).json({ error: "Credenciales inválidas" });

    const ok = await comparePassword(password, user.password);
    if (!ok) return res.status(401).json({ error: "Credenciales inválidas" });

    const encoder = new TextEncoder();

    const token = await new SignJWT({
      id: String(user._id),
      rol: user.rol,
      vipStatus: user.vipStatus,
    })
      .setProtectedHeader({ alg: "HS256" })
      .setIssuedAt()
      .setExpirationTime('1h')
      .sign(encoder.encode(process.env.JWT_SECRET));

    return res.json({
      token,
      user: {
        id: user._id,
        firstName: user.firstName,
        lastName: user.lastName,
        dni: user.dni,
        rol: user.rol,
        vipStatus: user.vipStatus,
      },
    });
  } catch (error) {
    return res.status(500).json({ error: "Error del servidor" });
  }
}
