import { userDatabaseModel, UserEntryData } from "./usersModel";
import mongoose from "mongoose";

/**
 * Controlador para el registro de un nuevo usuario
 * 
 * @async
 * @function registerUser
 * 
 * @description
 * Recibe los datos del nuevo usuario desde el cuerpo de la solicitud
 * Crea una instancia de UserEntryData con los datos recibidos
 * Valida los datos del usuario
 * Convierte los datos validados en un documento de Mongoose
 * Guarda el nuevo usuario en la base de datos
 * 
 * @param {import('express').Request} req - Objeto de solicitud de Express
 * @param {import('express').Response} res - Objeto de respuesta de Express
 * 
 * @returns {Promise} - Respuesta HTTP con:
 * - Código de estado 201 y mensaje de éxito si el usuario se crea correctamente
 * - Código de estado 400 y mensaje de error si hay un problema con los datos proporcionados
 * - Código de estado 500 y mensaje de error si hay un problema del servidor
 * 
 */
export async function registerUser(req, res) {
    try {
        const { firstName, lastName, password, dni, birthDate, cityName, gender} = req.body;
        const userEntry = new UserEntryData(firstName, lastName, password, dni, birthDate, cityName, gender, null, "Usuario", false);
        userEntry.validate();
        const userDB = userEntry.toDocument();
        await userDB.save();

        res.status(201).json({message: 'Usuario creado'});
    } catch (error) {
        return res.status(400).json({ error: error.message });

    }
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
        const { id, data} = req.body;
        if(!id) return res.status(400).json({ error: 'Se requiere el parametro de busqueda del usuario' });

        let user;

        if(data.includes("id")) {
            if(!mongoose.isValidObjectId(id)) return res.status(400).json({ error: 'No es un Id' });
            user = await userDatabaseModel.findById(id);
        } else {
            user = await userDatabaseModel.findOne({"dni": id});
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

/**
 * Controlador para la creación de un nuevo usuario con rol y estado VIP opcionales
 *
 * @async
 * @function createUser
 *
 * @description
 * Recibe los datos del nuevo usuario desde el cuerpo de la solicitud
 * Crea una instancia de UserEntryData con los datos recibidos
 * Valida los datos del usuario
 * Convierte los datos validados en un documento de Mongoose
 * Guarda el nuevo usuario en la base de datos
 * 
 * @param {import('express').Request} req - Objeto de solicitud de Express
 * @param {import('express').Response} res - Objeto de respuesta de Express
 * 
 * @returns {Promise} - Respuesta HTTP con:
 * - Código de estado 201 y mensaje de éxito si el usuario se crea correctamente
 * - Código de estado 400 y mensaje de error si hay un problema con los datos proporcionados
 */
export async function createUser(req, res) {
    try {
        const { firstName, lastName, password, dni, birthDate, cityName, gender, rol, vipStatus} = req.body;
        const userEntry = new UserEntryData(firstName, lastName, password, dni, birthDate, cityName, gender, null, rol, vipStatus);
        userEntry.validate();
        const userDB = userEntry.toDocument();
        await userDB.save();

        return res.status(201).json({message: 'Usuario creado, Rol adquirido: ' + rol});
    } catch (error) {
        return res.status(400).json({ error: error.message });

    }
}

/**
 * Controlador para actualizar un usuario existente
 * @async
 * @function updateUser
 *
 * @description In development
 */
export async function updateUser(req, res) {
    try {
        //Utilizar condicional para distintas posibilidades o crear otro (En Revision.)
    } catch (error) {
        return res.status(400).json({error: error.message})
    }
}