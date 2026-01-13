import mongoose from "mongoose";

const connectDB = async ()=> {
    try{
        await mongoose.connect(process.env.MONGO_URI);
        console.log("connected to mongo")
    }catch(err){
        console.error("error mongo",err.message)
        process.exit(1);
    }
}

export default connectDB;