import React, { useState } from 'react';
import axios from 'axios';
import styled from 'styled-components';

function UploadImages({ closeDialog, GroupID, onImageUpload }) {
    const [selectedFile, setSelectedFile] = useState(null);

    const handleFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
    };

    const handleUpload = async () => {
        try {
            const formData = new FormData();
            formData.append('file', selectedFile);
            const response = await axios.post(`http://localhost:8080/api/v1/message_group/Upload_Images/${GroupID}`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });

            console.log('Upload successful:', response.data);
            closeDialog();
            onImageUpload(); 
        } catch (error) {
            console.error('Error uploading image:', error);
        }
    };

    return (
        <DialogContainer>
            <DialogTitle>Chỉnh sửa ảnh</DialogTitle>
            <CloseButton onClick={closeDialog}>&times;</CloseButton>
            <UploadForm>
                <input type="file" onChange={handleFileChange} />
                <UploadButton onClick={handleUpload}>Lưu</UploadButton>
            </UploadForm>
        </DialogContainer>
    );
}

const DialogContainer = styled.div`
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: #0f0c29;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
    z-index: 1000;
`;

const DialogTitle = styled.h2`
    margin: 10px 0;
    color: #fff;
`;

const CloseButton = styled.button`
    position: absolute;
    top: 10px;
    right: 10px;
    background: none;
    border: none;
    font-size: 20px;
    cursor: pointer;
    color: red;
`;

const UploadForm = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    input {
        color: white;
    }
`;

const UploadButton = styled.button`
    margin-top: 10px;
    padding: 10px 20px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    
`;

export default UploadImages;
