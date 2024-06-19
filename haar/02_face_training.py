import cv2
import numpy as np
from PIL import Image, ImageEnhance
import os

# Path for face image database
path = 'dataset'
recognizer = cv2.face.LBPHFaceRecognizer_create()  # pylint: disable=no-member
detector = cv2.CascadeClassifier("C:\\capstone\\haar\\haarcascade_frontalface_default.xml")  # pylint: disable=no-member

def augment_image(image):
    augmented_images = []
    # Convert to PIL image for augmentation
    pil_img = Image.fromarray(image)
    
    # Apply different transformations
    for angle in [-15, -10, -5, 0, 5, 10, 15]:
        img_rot = pil_img.rotate(angle)
        augmented_images.append(np.array(img_rot))
    
    # Brightness adjustment
    enhancer = ImageEnhance.Brightness(pil_img)
    for factor in [0.8, 1.0, 1.2]:
        img_bright = enhancer.enhance(factor)
        augmented_images.append(np.array(img_bright))
    
    return augmented_images

# function to get the images and label data
def getImagesAndLabels(path):
    imagePaths = [os.path.join(path, f) for f in os.listdir(path)]     
    faceSamples = []
    ids = []
    for imagePath in imagePaths:
        PIL_img = Image.open(imagePath).convert('L')  # convert it to grayscale
        img_numpy = np.array(PIL_img, 'uint8')
        id = int(os.path.split(imagePath)[-1].split(".")[1])
        faces = detector.detectMultiScale(img_numpy)  # pylint: disable=no-member
        for (x, y, w, h) in faces:
            face = img_numpy[y:y+h, x:x+w]
            augmented_faces = augment_image(face)
            for augmented_face in augmented_faces:
                faceSamples.append(augmented_face)
                ids.append(id)
    return faceSamples, ids

print("\n [INFO] Training faces. It will take a few seconds. Wait ...")
faces, ids = getImagesAndLabels(path)
recognizer.train(faces, np.array(ids))  # pylint: disable=no-member

# Save the model into trainer/trainer.yml
recognizer.write('C:\\capstone\\haar\\trainer\\trainer.yml')  # recognizer.save() worked on Mac, but not on Pi
# Print the number of faces trained and end program
print("\n [INFO] {0} faces trained. Exiting Program".format(len(np.unique(ids))))
