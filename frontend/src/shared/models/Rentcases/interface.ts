import { IRentCaseStatus } from "../RentCaseStatus";
import { ILessee } from "../Lessee";
import { ILessor } from "../Lessor";

export interface IRentCase {
    rentCaseId: number;
    rentAmount: number;
    dueDate: string;
    rentCaseStatus: IRentCaseStatus;
    lessor: ILessor;
    lessee: ILessee;
    isSent: number;
}