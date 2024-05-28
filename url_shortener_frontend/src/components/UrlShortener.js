import React, { useState } from "react";
import axiosInstance from "../utils/axiosInstance";
import "./UrlShortener.css";

function UrlShortener() {
  const [originalUrl, setOriginalUrl] = useState("");
  const [shortUrl, setShortUrl] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    const response = await axiosInstance.post("/api/shorten", { originalUrl });
    setShortUrl(response.data);
  };

  return (
    <div className="url-shortener-container">
      <h1>URL Shortener</h1>
      <form className="url-shortener-form" onSubmit={handleSubmit}>
        <input
          type="text"
          className="url-shortener-input"
          value={originalUrl}
          onChange={(e) => setOriginalUrl(e.target.value)}
          placeholder="Enter URL"
        />
        <button type="submit" className="url-shortener-button">
          Shorten URL
        </button>
      </form>
      {shortUrl && (
        <div className="short-url-container">
          <p>Short URL: {shortUrl}</p>
          <a
            href={process.env.REACT_APP_API_BASE_URL + "/api/" + shortUrl}
            target="_blank"
            rel="noopener noreferrer"
          >
            Visit
          </a>
        </div>
      )}
    </div>
  );
}

export default UrlShortener;
