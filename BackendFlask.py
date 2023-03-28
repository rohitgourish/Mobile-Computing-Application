from flask import Flask
from flask import request
import base64
from PIL import Image, ImageOps
import io
import datetime
import os
import cv2

from keras.models import load_model
import numpy as np

dirPath = '/Users/gk/Downloads/'
model_name = 'mnist2.h5'
path = dirPath + {model_name}
model = load_model(path)
UPLOAD_FOLDER = '/Users/gk/Desktop/'

app=Flask(__name__)

@app.route('/')
def welcome():
    return 'You are now connected to the server'


@app.route('/upload',methods=['POST'])
def upload():
       text = request.form['category']
       image = base64.b64decode(text) 
       img = Image.open(io.BytesIO(image))

      
       original = img
       img = img.resize((28,28))
       img = img.convert('L')
       # (thresh, im_bw) = cv2.threshold(img, 128, 255, cv2.THRESH_BINARY | cv2.THRESH_OTSU)
       img = ImageOps.invert(img)
       img = np.array(img)
       img_bw = (img > 150) * img
       img = img.reshape(1,28,28,1)
       img_bw = img_bw.reshape(1,28,28,1)
       img = img/255.0
       img_bw = img_bw/255.0
       res = np.argmax(model.predict([img])[0])
       res1 = np.argmax(model.predict([img_bw])[0])
       # fileName = f'{str(res)}-{datetime.datetime.now().strftime("%Y-%m-%d--%H-%M-%S")}.jpeg'
       fileName = f'{str(res1)}-{datetime.datetime.now().strftime("%Y-%m-%d--%H-%M-%S")}.jpeg'
       dirPath = (dirPath+str(res1))
       isExist = os.path.exists(dirPath)
       # print(res)
       # print(res1)
       # Create a new directory because it does not exist 
       if not isExist:
              os.makedirs(dirPath,mode = 0o777)
       imagePath = (dirPath+"/"+fileName)
       original.save(imagePath, 'jpeg')
       print("image uploaded successfully")
       return "success"


if __name__=="__main__":
   app.run(host='0.0.0.0',debug=True,port=8000)