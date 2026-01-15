import { Schema, model } from 'mongoose';

/**
 * @typedef userSchema
 * 
 * @property {import('mongoose').Types.ObjectId} _id - Identificador único creado por MongoDB
 * @property {string} firstName - Nombre propio del usuario
 * @property {string} lastName - Apellido/s del usuario
 * @property {string} password - Contraseña del usuario
 * @property {string} dni - Documento de identidad del usuario
 * @property {Date} birthDate - Fecha de nacimiento del usuario
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
    lastName: {
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
    birthDate: {
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
    constructor(firstName, lastName, password, dni, birthDate, cityName, gender, imageRoute, rol, vipStatus) {
        this.firstName = firstName
        this.lastName = lastName
        this.password = password
        this.dni = dni
        this.birthDate = birthDate
        this.cityName = cityName
        this.gender = gender
        this.imageRoute = imageRoute
        this.rol = rol || "Usuario"
        this.vipStatus = vipStatus || false
        
        this.ready = false
    }

    validate() {
        if (!this.firstName || !this.lastName) throw new Error("Nombre y Apellido no pueden estar vacíos.");
        if(/\d/.test(this.firstName) || /\d/.test(this.lastName)) throw new Error("Nombre y Apellido no pueden contener números.");
        if(this.password.length < 8) throw new Error("La contraseña tiene que contener al menos de 8 caracteres.");
        if(this.birthDate.getTime() < Date.now() - 504921600000) throw new Error("Tienes que ser mayor de 16 años.")
        if(!["Hombre", "Mujer"].includes(this.gender)) throw new Error("Seleccione un Genero");
        if(!/^\d{8}[a-zA-Z]$/.test(this.dni)) throw new Error("DNI Incorrecto.");
        
        let letterNum = parseInt(this.dni.slice(0,8));
        const letterDni = ["T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", "N", "J", "Z", "S", "Q", "V", "H", "L", "C", "K", "E"];
        if(this.dni[8] !== letterDni[letterNum%23]) throw new Error("DNI Incorrecto");
        
        this.ready = true
    }


    toDocument() {
        if(!this.ready) throw new Error("Completa la creación de usuario correctamente.")
        return new userDatabaseModel({
            firstName: this.firstName,
            lastName: this.lastName,
            password: this.password,
            dni: this.dni,
            birthDate: this.birthDate,
            cityname: this.cityName,
            gender: this.gender,
            imageRoute: this.imageRoute,
            rol: this.rol,
            vipStatus: this.vipStatus
        });
    }
}