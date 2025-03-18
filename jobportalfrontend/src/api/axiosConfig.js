import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080", // Make sure this matches your backend URL
  headers: {
    "Content-Type": "application/json",
  },
});

export default api;
