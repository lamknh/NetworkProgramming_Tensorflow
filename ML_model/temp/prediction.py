import os
import numpy as np
from PIL import Image
import matplotlib.pyplot as plt
import json
import requests
import sys # 자바에서 시스템 명령어로 파일 경로를 매개변수로 받아오기 위함

IMAGE_WIDTH=128
IMAGE_HEIGHT=128
IMAGE_SIZE=(IMAGE_WIDTH, IMAGE_HEIGHT)
IMAGE_CHANNELS=3
batch_size=15

def image_to_array(file_path):
    img = Image.open(file_path)
    img = img.resize((IMAGE_WIDTH, IMAGE_HEIGHT))
    data = np.asarray(img,dtype='float32')
    return data

test_images = []
test_img = image_to_array(sys.argv[1]) #image경로 각자 맞게 설정 필요. # sys.argv[1] 부분이 파일 경로 부분
test_img = test_img / 255           #0~1 사이의 값을 가지도록 해줌.
test_images.append(test_img)        
test_images = np.array(test_images) #numpy array로 변환.

#model에 값을 넘겨줄 때 모델의 입력형태에 맞게끔 차원 확장. (모델에 (n, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_CHANNEL)의 형태로 입력 넣어줘야함)
test_images = test_images.reshape(test_images.shape[0], IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_CHANNELS)

data = json.dumps({"signature_name": "serving_default",
                   "instances": test_images[0:1].tolist()})

headers = {"content-type": "application/json"}

json_response = requests.post('http://localhost:8501/v1/models/cats-vs-dogs_model:predict',
                              data=data,
                              headers=headers)
                            
predictions = json.loads(json_response.text)
cat_or_dog = np.argmax(predictions['predictions'], axis=-1)

with open('/Users/Public/result.txt', 'w') as f: # 파일 출력 경로
    if cat_or_dog[0] == 0:
        f.write('Cat') # 결과값 반환
    else:
        f.write('Dog')

