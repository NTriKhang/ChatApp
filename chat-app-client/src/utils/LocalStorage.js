export const getCurrentUserLocal = () => {
    return JSON.parse(localStorage.getItem("chat-app-current-user"));
}

export const setCurrentUserLocal = (data) => {
    return  localStorage.setItem(
        "chat-app-current-user",
        JSON.stringify(data)
      );;
}