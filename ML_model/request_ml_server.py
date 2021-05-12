import json
import requests
import numpy as np
import matplotlib.pyplot as plt

# import load_model
from tensorflow.keras.models import load_model
# importing the libraries
from tensorflow.keras import datasets

# loading dataset
(train_images, train_labels), (test_images, test_labels) = datasets.mnist.load_data()

# For training, we will use 10000 images 
# And we will test our model on 1000 images
test_labels = test_labels[:1000]

test_images = test_images[:1000].reshape(-1, 28 * 28) / 255.0

# create a json string to ask query to the depoyed model
data = json.dumps({"signature_name": "serving_default",
                   "instances": test_images[0:3].tolist()})
print("test label :", test_labels[0:3])

# function to display image
def show(idx, title):
    plt.figure()
    plt.imshow(test_images[idx].reshape(28,28))
    plt.axis('off')
    plt.title('\n\n{}'.format(test_labels[idx]), fontdict={'size': 16})

# headers for the post request
headers = {"content-type": "application/json"}

# make the post request 
# 8501 port is REST API port. So, we must use this port to communicate ML model.
# 
json_response = requests.post('http://localhost:8501/v1/models/test_model:predict',
                              data=data,
                              headers=headers)

# get the predictions
predictions = json.loads(json_response.text)
# get the prediction array
predictions = predictions['predictions']

print(np.shape(predictions))

for i, prediction in enumerate(predictions):
    print("Prediction: ",np.argmax(prediction))
    show(i,test_images[i])

plt.show()