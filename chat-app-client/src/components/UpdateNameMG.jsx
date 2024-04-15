import React, { useState } from 'react';
import axios from 'axios';
import styled from 'styled-components';

function UpdateNameMG({ handleClose, groupId, updateGroupName }) {
    const [newGroupName, setNewGroupName] = useState('');

    const handleUpdate = async () => {
        try {
            const response = await axios.put(`http://localhost:8080/api/v1/message_group`, {
                _id: groupId,
                Message_group_name: newGroupName
            });
            if (response.status === 200) {
                alert('Update Success')
                console.log('Update successful:', response.data);
                updateGroupName(newGroupName);
                handleClose();
            } else {
                console.error('Error updating message group:', response.statusText);
            }
        } catch (error) {
            console.error('Error updating message group:', error.message);
        }
    };

    return (
        <DialogOverlay>
            <DialogContainer>
                <DialogTitle>Chỉnh sửa tên nhóm</DialogTitle>
                <DialogContent>
                    <InputWrapper>
                        <label htmlFor="newGroupName">Tên nhóm mới:</label>
                        <input
                            type="text"
                            id="newGroupName"
                            value={newGroupName}
                            onChange={(e) => setNewGroupName(e.target.value)}
                        />
                    </InputWrapper>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleUpdate}>Lưu</Button>
                    <Button onClick={handleClose}>Hủy</Button>
                </DialogActions>
            </DialogContainer>
        </DialogOverlay>
    );
}

export default UpdateNameMG;

const DialogOverlay = styled.div`
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5); /* Màu đen làm mờ */
    display: flex;
    justify-content: center;
    align-items: center;
    z-index:999;
`;

const DialogContainer = styled.div`
    background-color: #0f0c29;
    color: #fff;
    border-radius: 8px;
    padding: 20px;
    width: 400px;
`;

const DialogTitle = styled.div`
    font-size: 20px;
    font-weight: bold;
    margin-bottom: 10px;
    color: #fff; 
`;

const DialogContent = styled.div`
    margin-bottom: 20px;
`;

const InputWrapper = styled.div`
    margin-bottom: 10px;

    label {
        display: block;
        margin-bottom: 5px;
    }

    input {
        width: 100%;
        padding: 8px;
        border: 1px solid #ccc;
        border-radius: 5px;
    }
`;

const DialogActions = styled.div`
    display: flex;
    justify-content: flex-end;
`;

const Button = styled.button`
    margin-left: 10px;
    padding: 8px 16px;
    border: none;
    border-radius: 5px;
    background-color: #007bff; /* Màu nền xanh */
    color: #fff;
    cursor: pointer;

    &:hover {
        background-color: #0056b3; /* Màu nền xanh nhạt khi hover */
    }
`;
