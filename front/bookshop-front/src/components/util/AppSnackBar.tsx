import {useEffect} from 'react';
import {useSnackbar} from 'notistack';
import {useSocket} from "../socket/SocketContext";
import {Notifications} from "../../enums/Notifications";

function AppSnackbar() {
    const {enqueueSnackbar} = useSnackbar();
    const socket = useSocket();

    const appNotificationHandler = () => {
        enqueueSnackbar(Notifications.NEW_MESSAGE, {variant: 'info'});
    }

    useEffect(() => {
        if (socket) {
            socket.on('newMessage', appNotificationHandler);

            return () => {
                socket.off('newMessage');
            };
        }
    }, [socket]);

    useEffect(() => {

    }, [enqueueSnackbar]);

    return null;
}

export default AppSnackbar;