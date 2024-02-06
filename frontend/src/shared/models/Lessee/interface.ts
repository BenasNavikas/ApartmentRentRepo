import { ILessor } from "../Lessor";
import { IUser } from "../User";

export interface ILessee {
    id: number;
    name: string;
    address?: string;
    phoneNumber?: string;
    email?: string;
    accountNumber?: string;
    lessor: ILessor;
    user: IUser;
}