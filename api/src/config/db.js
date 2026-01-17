import mongoose from "mongoose";

const connectDB = async ()=> {
    try{
        await mongoose.connect(process.env.MONGO_URI);
        console.log("Conexión a mongo completada")
    }catch(err){
        console.error("Error de mongo",err.message)
        process.exit(1);
    }
}

export default connectDB;