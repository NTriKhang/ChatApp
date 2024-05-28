export const getCurrentUserLocal = () => {
    // let check = {}
    // if (localStorage.getItem("chat-app-current-user") === check) {
    //     var usernull = {
    //         _id : ''
    //     }
    //     console.log(usernull)
    //     return usernull
    // }
    return JSON.parse(localStorage.getItem("chat-app-current-user"));
}

export const setCurrentUserLocal = (data) => {
    return localStorage.setItem(
        "chat-app-current-user",
        JSON.stringify(data)
    );
}

export const getConnectStateLocal = () => {
    return localStorage.getItem("chat-app-connect-state");
}

export const setConnectStateLocal = (state) => {
    return localStorage.setItem(
        "chat-app-connect-state",
        state
    );
}