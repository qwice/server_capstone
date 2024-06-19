import cv2  # pylint: disable=no-member
import numpy as np
import os

recognizer = cv2.face.LBPHFaceRecognizer_create()  # pylint: disable=no-member
recognizer.read('C:\\capstone\\haar\\trainer\\trainer.yml')  # pylint: disable=no-member
cascadePath = "C:\\capstone\\haar\\haarcascade_frontalface_default.xml"
faceCascade = cv2.CascadeClassifier(cascadePath)  # pylint: disable=no-member
font = cv2.FONT_HERSHEY_SIMPLEX  # pylint: disable=no-member

# iniciate id counter
id = 0

# names related to ids: example ==> loze: id=1, etc
names = ['None', 'Seoyoung']

# Initialize and start realtime video capture
cam = cv2.VideoCapture(0)  # pylint: disable=no-member
cam.set(3, 640)  # set video width  # pylint: disable=no-member
cam.set(4, 480)  # set video height  # pylint: disable=no-member

# Define min window size to be recognized as a face
minW = 0.1 * cam.get(3)  # pylint: disable=no-member
minH = 0.1 * cam.get(4)  # pylint: disable=no-member

while True:
    ret, img = cam.read()  # pylint: disable=no-member
    if not ret:
        break
    # img = cv2.flip(img, -1) # Flip vertically
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)  # pylint: disable=no-member

    faces = faceCascade.detectMultiScale(  # pylint: disable=no-member
        gray,
        scaleFactor=1.2,
        minNeighbors=5,
        minSize=(int(minW), int(minH)),
    )

    for (x, y, w, h) in faces:
        cv2.rectangle(img, (x, y), (x + w, y + h), (0, 255, 0), 2)  # pylint: disable=no-member
        id, confidence = recognizer.predict(gray[y:y + h, x:x + w])  # pylint: disable=no-member
        # Check if confidence is less than 100 ==> "0" is a perfect match
        if confidence < 100:
            id = names[id]
            confidence = "  {0}%".format(round(100 - confidence))
        else:
            id = "unknown"
            confidence = "  {0}%".format(round(100 - confidence))

        cv2.putText(img, str(id), (x + 5, y - 5), font, 1, (255, 255, 255), 2)  # pylint: disable=no-member
        cv2.putText(img, str(confidence), (x + 5, y + h - 5), font, 1, (255, 255, 0), 1)  # pylint: disable=no-member

    cv2.imshow('camera', img)  # pylint: disable=no-member
    k = cv2.waitKey(10) & 0xff  # Press 'ESC' for exiting video  # pylint: disable=no-member
    if k == 27:
        break
# Do a bit of cleanup
print("\n [INFO] Exiting Program and cleanup stuff")
cam.release()  # pylint: disable=no-member
cv2.destroyAllWindows()  # pylint: disable=no-member
