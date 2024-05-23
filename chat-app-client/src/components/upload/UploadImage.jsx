import { useState, useEffect } from "react";
import { Modal, Upload } from "antd";

const getBase64 = (file) =>
  new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = (error) => reject(error);
  });

const UploadImage = ({ onChangeImage, isIconSelect }) => {
  const [imageUrl, setImageUrl] = useState();
  const [previewOpen, setPreviewOpen] = useState(false);
  const [previewImage, setPreviewImage] = useState("");
  const [previewTitle, setPreviewTitle] = useState("");

  useEffect(() => {
    onChangeImage(imageUrl);
  }, [imageUrl]);

  const handleUploadChange = (info) => {
    setImageUrl({
      name: info.file.name,
      uid: info.file.uid,
      url: info.file?.response?.data?.image_url,
    });
  };

  const handleCancel = () => setPreviewOpen(false);

  const handlePreview = async (file) => {
    if (!file.url && !file.preview) {
      file.preview = await getBase64(file.originFileObj);
    }

    setPreviewImage(file.url || file.preview);
    setPreviewOpen(true);
    setPreviewTitle(
      file.name || file.url.substring(file.url.lastIndexOf("/") + 1)
    );
  };

  const uploadButton = (
    <div>
      <span class="material-symbols-outlined">add</span>
      <div style={{ marginTop: 8 }}>Thêm ảnh</div>
    </div>
  );

  return (
    <>
      <div>
        <Upload
          action={`https://api.microlap.vn/upload/image`}
          listType={!isIconSelect ? "picture-card" : "text"}
          onChange={handleUploadChange}
          showUploadList={false}
          onPreview={handlePreview}
        >
          {!isIconSelect ? (
            imageUrl?.url ? (
              <img
                src={imageUrl?.url}
                alt="avatar"
                style={{ width: "100%", height: "100%", objectFit: "cover" }}
              />
            ) : (
              uploadButton
            )
          ) : (
            <span class="material-symbols-outlined fs-2 text-white me-3 cursor-pointer">add_a_photo</span>
          )}
        </Upload>
      </div>
      <Modal
        footer={null}
        onCancel={handleCancel}
        open={previewOpen}
        title={previewTitle}
      >
        <img alt="example" src={previewImage} style={{ width: "100%" }} />
      </Modal>
    </>
  );
};

export { UploadImage };
