import { Schema, model } from 'mongoose';

/**
 * @typedef userSchema
 * 
 * @property {import('mongoose').Types.ObjectId} _id - Identificador único creado por MongoDB
 * @property {string} firstName - Nombre propio del usuario
 * @property {string} secondName - Apellido/s del usuario
 * @property {string} password - Contraseña del usuario
 * @property {string} dni - Documento de identidad del usuario
 * @property {Date} bornDate - Fecha de nacimiento del usuario
 * @property {string} cityName - Ciudad de vivienda fiscal
 * @property {"Hombre" | "Mujer"} gender - Genero del usuario
 * @property {string} imageRoute - Ruta de la imagen del usuario
 * @property {"Admin" | "Trabajador" | "Usuario"} rol - Rol del usuario
 * @property {Boolean} vipStatus - Estado membresia VIP del usuario, "true" cuenta con membresia acctiva, "false" no cuenta con membresia
 * 
 * @description Documento correspondiente a los datos en MongoDB sobre el usuario
*/
const userDatabaseSchema = new Schema({
    firstName: {
        type: String,
        required: true,
        trim: true
    },
    secondName: {
        type: String,
        required: true,
        trim: true
    },
    password: {
        type: String,
        required: true,
        select: true
    },
    dni: {
        type: String,
        required: true,
        unique: true,
        trim: true
    },
    bornDate: {
        type: Date,
        required: true
    },
    cityName: {
        type: String,
        required: true,
        trim: true
    },
    gender: {
        type: String,
        enum: ["Hombre", "Mujer"],
        required: true
    },
    imageRoute: {
        type: String,
        required: false
    },
    rol: {
        type: String,
        enum: ["Admin", "Trabajador", "Usuario"],
        default: "Usuario"
    },
    vipStatus: {
        type: Boolean,
        default: false
    }
});

export const userDatabaseModel = model('user', userDatabaseSchema)

class UserEntryData {
    constructor(firstName, secondName, password, dni, bornDate, cityName, gender, imageRoute, rol, vipStatus) {
        this.firstName = firstName
        this.secondName = secondName
        this.password = password
        this.dni = dni
        this.bornDate = bornDate
        this.cityName = cityName
        this.gender = gender
        this.imageRoute = imageRoute
        this.rol = rol || "Usuario"
        this.vipStatus = vipStatus || false
        
    }
}