import React, { useState } from "react";
import styled from "styled-components";

export default function SearchBar({ onSearch }) {
  const [searchTerm, setSearchTerm] = useState("");

  const handleSearchChange = (event) => {
    setSearchTerm(event.target.value);
    // Call the onSearch function with the current search term
    onSearch(event.target.value);
  };

  return (
    <SearchContainer>
      <SearchInput
        type="text"
        placeholder="@Tag"
        value={searchTerm}
        onChange={handleSearchChange}
      />
      <SearchButton>
      <span class="material-symbols-outlined">
        search
      </span>
      </SearchButton>
    </SearchContainer>
  );
}

const SearchContainer = styled.div`
  display: flex;
  align-items: center;
  position: relative;
  margin-top: 5px;
`;

const SearchInput = styled.input`
  flex: 1;
  padding: 10px 45px 10px 15px;
  border-radius: 20px;
  outline: none;
  background-color: #f3f4f6;
  color: #555; 
  font-size: 16px;
`;

const SearchButton = styled.button`
  position: absolute;
  right: 10px;
  top: 4px;
  bottom: 0;
  padding: 5px;
  cursor: pointer;
  outline: none;
  img {
    width: 30px;
    height: 20px;
  }
`;
