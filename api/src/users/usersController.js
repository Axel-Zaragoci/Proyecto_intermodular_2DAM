import { userDatabaseModel, UserEntryData } from "./usersModel";
import mongoose from "mongoose";

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