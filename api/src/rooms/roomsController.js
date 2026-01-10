import { roomDatabaseModel,RoomEntryData } from "./roomsModel";

export const createRoom = async(req,res) =>{
    try{
        const{
            type,roomNumber,maxGuests,description,mainImage,pricePerNight,
            extraBed,crib,offer,extras,extraImages
        } = req.body
        
        const entry = new RoomEntryData(
            type,
            roomNumber,
            maxGuests,
            description,
            mainImage,
            pricePerNight);
        
        entry.completeRoomData(
            extraBed ?? false,
            crib ?? false,
            offer ?? 0,
            extras ?? [],
            extraImages ?? []
        );

        const doc = entry.toDocument();
        const saved = await doc.save();
    
        return res.status(201).json(saved);

    }catch(err){
  
    if (err?.code === 11000) {
        return res.status(409).json({ message: "roomNumber ya existe" });
      }
      return res.status(400).json({ message: err.message });
    }
}

export const getAllRooms = async(req,res) => {
    try {
        
        const rooms = await roomDatabaseModel.find();
        return res.json(rooms);

      } catch (err) {
        return res.status(500).json({ message: err.message });
      }
}

export const getRoomById = async(req,res) => {
    try{
        const {id} = req.params;
        const room = await roomDatabaseModel.findById(id);
        
        if(!room) return res.status(404).json({message:"no se encontro esa habitacion"})

        return res.json(room)
    }catch(err){
        return res.status(500).json({message:err.message});
    }
}

export const updateRoom = async (req,res) => {
    try{
        const {id} = req.params

        const updated = await roomDatabaseModel.findByIdAndUpdate(
            id,
            { $set: req.body },
            { new: true, runValidators: true }
          );
      
          if (!updated) return res.status(404).json({ message: "Room no encontrada" });

          return res.json(updated);
    }catch(err){

    }
}

export const deleteRoom = async (req, res) => {
    try {
      
        const { id } = req.params;

        const deleted = await roomDatabaseModel.findByIdAndDelete(id);
      
        if (!deleted) return res.status(404).json({ message: "Room no encontrada" });
      
        return res.json({ message: "Room eliminada", deleted });
        
    } catch (err) {
      return res.status(400).json({ message: err.message });
    }
  };
