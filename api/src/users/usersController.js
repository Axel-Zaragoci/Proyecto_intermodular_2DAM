import { userDatabaseModel, UserEntryData } from "./usersModel.js";
import mongoose from "mongoose";
import { hashPassword } from "../services/password.service.js";

/**
 *  Controlador para el registro de un nuevo usuario
 * 
 * @async
 * @function register
 * @description
 * Recibe los datos del nuevo usuario desde el cuerpo de la solicitud
 * Comprueba si tiene todos los datos necesarios
 * Determina si la solicitud es pública o realizada por un usuario autenticado
 * En base a los permisos del usuario que realiza la solicitud, crea un nuevo usuario como un registro cualquiera o permite asignar rol y estado VIP
 * Valida los datos del usuario
 * Convierte los datos validados en un documento de Mongoose
 * Guarda el nuevo usuario en la base de datos 
 * 
 * @param {import('express').Request} req - Objeto de solicitud de Express
 * @param {import('express').Response} res - Objeto de respuesta de Express
 */
export async function register(req, res) {
    try {
        const isPublic = !req.user;
        const { firstName, lastName, email, password, dni, birthDate, cityName, gender } = req.body;
        const birthDateObj = new Date(birthDate);

        if (!firstName) return res.status(400).json({ error: "El nombre no puede estar vacio." });
        if (!lastName) return res.status(400).json({ error: "El apellido no puede estar vacio." });
        if (!email) return res.status(400).json({errors: "El correo no puede estar vacio"});
        if (!password) return res.status(400).json({ error: "La contraseña no puede estar vacia." });
        if (!dni) return res.status(400).json({ error: "El dni no puede estar vacio." });
        if (Number.isNaN(birthDateObj.getTime())) return res.status(400).json({ error: "La fecha no puede estar vacia." });
        if (!cityName) return res.status(400).json({ error: "La ciudad no puede estar vacia." });
        if (!gender) return res.status(400).json({ error: "Tienes que seleccionar un genero." });

        const passHash = await hashPassword(password);
        
        const baseData = { firstName, lastName, email, password: passHash, dni, birthDate: birthDateObj, cityName, gender };

        let userEntry;

        
        if (isPublic) {
            userEntry = new UserEntryData(baseData.firstName, baseData.lastName, email, baseData.password, baseData.dni, baseData.birthDate, baseData.cityName, baseData.gender, null);
        } else{
            const { rol, vipStatus } = req.body;
            userEntry = createBy(req, baseData, { rol, vipStatus });
        }

        userEntry.validate();
        const userDB = userEntry.toDocument();
        await userDB.save();

    } catch(error) {
        console.error('Error al registrar al usuario:', error);
        return res.status(500).json({ error: 'Error del servidor' })
    }
}

/**
 * Permite la creación de un usuario con rol y estado VIP en función de los permisos del usuario que realiza la solicitud
 * @function createBy
 * @description
 * Recibe el usuario que realiza la solicitud, los datos base del nuevo usuario y los datos opcionales de rol y estado VIP
 * Dependiendo del rol del usuario que realiza la solicitud, permite o deniega la asignación de rol y estado VIP al nuevo usuario
 * Devuelve una instancia de UserEntryData con los datos del nuevo usuario
 * 
 * @param {import('express').Request} req
 * @param {Object} baseData - Datos base del nuevo usuario
 * @param {Object} param2 - Datos opcionales del nuevo usuario
 * @param {string} param2.rol - Rol del nuevo usuario
 * @param {boolean} param2.vipStatus - Estado VIP del nuevo usuariolo
 * @returns {UserEntryData} - 
 */
function createBy(req, baseData, { rol, vipStatus }) {
    const actorRol = req.user.rol;

    if (actorRol === "Trabajador") {
        if (rol !== "Usuario") throw new Error("Solamente puedes crear Usuarios.");
    } else if (actorRol === "Admin") {
        if (rol !== "Usuario" && rol !== "Empleado") throw new Error("Un Admin solo puede crear Usuario o Empleado.");
    } else {
        throw new Error("No tienes permisos para crear usuarios con rol y estado VIP.");
}
    return new UserEntryData(baseData.firstName, baseData.lastName, baseData.password, baseData.dni, baseData.birthDate, baseData.cityName, baseData.gender, null, rol, vipStatus);
}

/**
 * Controlador para obtener un usuario por su ID o DNI
 * 
 * @async
 * @function getOneUserByIdOrDni
 * 
 * @description
 * Recibe el ID o DNI del usuario desde el cuerpo de la solicitud
 * Verifica si el parámetro de búsqueda está presente
 * Si el parámetro de búsqueda es un ID, valida su formato y busca el usuario por ID
 * Si el parámetro de búsqueda es un DNI, busca el usuario por DNI
 * Devuelve el usuario encontrado o un mensaje de error si no se encuentra
 * Maneja errores del servidor y devuelve un mensaje de error adecuado
 * 
 * @param {import('express').Request} req - Objeto de solicitud de Express
 * @param {import('express').Response} res - Objeto de respuesta de Express
 * 
 * @returns {Promise} - Respuesta HTTP con:
 * - Código de estado 200 y el usuario si se encuentra correctamente
 * - Código de estado 400 y mensaje de error si el ID proporcionado no es válido o falta el parámetro de búsqueda
 * - Código de estado 404 y mensaje de error si el usuario no se encuentra
 * - Código de estado 500 y mensaje de error si hay un problema del servidor
 */
export async function getOneUserByIdOrDni(req, res) {
    try {
        const { searchData, searchProperty} = req.body;
        if(!searchData) return res.status(400).json({ error: 'Se requiere el parametro de busqueda del usuario' });

        let user;

        if(searchProperty.includes("id")) {
            if(!mongoose.isValidObjectId(searchData)) return res.status(400).json({ error: 'No es un Id' });
            user = await userDatabaseModel.findById(searchData);
        } else {
            user = await userDatabaseModel.findOne({"dni": searchData});
        }

        if(!user) return res.status(404).json({ error: 'Usuario no encontrado' });
        
        return res.status(200).json(user)
    } catch (error) {
        console.error('Error al obtener el usuario:', error);
        return res.status(500).json({ error: 'Error del servidor' })
    }
}

/**
 * Controlador para obtener todos los usuarios
 * 
 * @async
 * @function getAllUsers
 * 
 * @description
 * Busca todos los usuarios en la base de datos
 * Devuelve los usuarios encontrados o un mensaje de error si hay un problema
 * 
 * @param {import('express').Request} req - Objeto de solicitud de Express
 * @param {import('express').Response} res - Objeto de respuesta de Express
 * 
 * @returns {Promise} - Respuesta HTTP con:
 * - Código de estado 200 y los usuarios si se encuentran correctamente
 * - Código de estado 500 y mensaje de error si hay un problema del servidor
 */
export async function getAllUsers(req, res) {
    try {
        const users =  await userDatabaseModel.find();
        return res.status(200).json(users);
    } catch (error) {
        console.error('Error al obtener los usuarios:', error);
        return res.status(500).json({ error: 'Error del servidor' })
    }
}

/**
 * Controlador para obtener usuarios por su rol
 * 
 * @async
 * @function getUsersByRol
 * 
 * @description
 * Recibe el rol desde los parámetros de la solicitud
 * Verifica si el rol es válido
 * Busca los usuarios con el rol especificado en la base de datos
 * Devuelve los usuarios encontrados o un mensaje de error si hay un problema
 * 
 * @param {import('express').Request} req - Objeto de solicitud de Express
 * @param {import('express').Response} res - Objeto de respuesta de Express
 * 
 * @returns {Promise} - Respuesta HTTP con:
 * - Código de estado 200 y los usuarios si se encuentran correctamente
 * - Código de estado 400 y mensaje de error si el rol proporcionado no es válido
 * - Código de estado 500 y mensaje de error si hay un problema del servidor
 */
export async function getUsersByRol(req, res) {
    try {
        const { rol } = req.params;

        if(!["Admin", "Trabajador", "Usuario"].includes(rol)) {return res.status(400).json({ error: 'Rol no válido' });}

        const users = await userDatabaseModel.find({ rol });
        return res.status(200).json(users);
    } catch (error) {
        console.error('Error al obtener los usuarios por rol:', error);
        return res.status(500).json({ error: 'Error del servidor' })
    }
}