import {Dispatch, SetStateAction} from "react";

export default interface ITicket {
    username: string,
    title: string,
    lastMessage: string,
    isRead: boolean,
    uuid: string,
    timestamp: string
    setActiveChat: Dispatch<SetStateAction<string | null>>;
}