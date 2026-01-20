import bcrypt from 'bcryptjs';
const SALT_ROUNDS = 12;
/**
 * Hasheado de la contraseña
 * 
 * @async
 * @function hashPassword
 * 
 * @param {string} password
 * @description
 * Recibe la contraseña y realiza validaciones
 * Si pasa las validaciones encripta la contraseña y la devuelve 
 */
export async function hashPassword(password) {
    if (typeof password !== 'string' || password.trim().length === 0) throw new Error('Contraseña no puede estar vacia.');
    return await bcrypt.hash(password, SALT_ROUNDS);
}
