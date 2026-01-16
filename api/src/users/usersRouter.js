import { Router } from 'express';
import { getOneUserByIdOrDni, getAllUsers, getUsersByRol, registerUser, createUser } from './usersController.js';

const usersRouter = Router();

usersRouter.get('/', getAllUsers);
usersRouter.get('/rol/:rol', getUsersByRol);
usersRouter.get('/getOne', getOneUserByIdOrDni);

usersRouter.post('/register', registerUser);
usersRouter.post('/', createUser);

export default usersRouter;