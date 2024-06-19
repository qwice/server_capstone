import cv2  # pylint: disable=no-member
import os

cam = cv2.VideoCapture(0)  # pylint: disable=no-member
cam.set(3, 640)  # pylint: disable=no-member
cam.set(4, 480)  # pylint: disable=no-member
face_detector = cv2.CascadeClassifier('C:\\capstone\\haar\\haarcascade_frontalface_default.xml')  # pylint: disable=no-member

# For each person, enter one numeric face id
face_id = input('\n enter user id end press <return> ==>  ')
print("\n [INFO] Initializing face capture. Look the camera and wait ...")

# Initialize individual sampling face count
count = 0
while(True):
    ret, img = cam.read()  # pylint: disable=no-member
    # img = cv2.flip(img, -1) # flip video image vertically
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)  # pylint: disable=no-member
    faces = face_detector.detectMultiScale(gray, 1.3, 5)  # pylint: disable=no-member

    for (x, y, w, h) in faces:
        cv2.rectangle(img, (x, y), (x + w, y + h), (255, 0, 0), 2)  # pylint: disable=no-member
        count += 1

        # Save the captured image into the datasets folder
        cv2.imwrite("dataset/User." + str(face_id) + '.' + str(count) + ".jpg", gray[y:y + h, x:x + w])  # pylint: disable=no-member

        cv2.imshow('image', img)  # pylint: disable=no-member

    k = cv2.waitKey(500) & 0xff  # pylint: disable=no-member  # Press 'ESC' for exiting video
    if k == 27:
        break
    elif count >= 500:  # Take 30 face sample and stop video
        break

# Do a bit of cleanup
print("\n [INFO] Exiting Program and cleanup stuff")
cam.release()  # pylint: disable=no-member
cv2.destroyAllWindows()  # pylint: disable=no-member
