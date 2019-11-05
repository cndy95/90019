import os
import urllib.request


import numpy as np
import pandas as pd
import caffe
import socket

mean_file = 'ilsvrc_2012_mean.npy'
deploy_path = 'sentiment_deploy.prototxt'
caffemodel_path ='twitter_finetuned_test4_iter_180.caffemodel'
net = caffe.Classifier(deploy_path, caffemodel_path, mean=np.load(mean_file).mean(1).mean(1), image_dims=(256,256), channel_swap=(2,1,0), raw_scale=255)




serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
host =  '45.113.234.239'
port = 65432
serversocket.bind((host, port))
serversocket.listen(10)
idx = 0
clientsocket = None
try:
    while 1:
        clientsocket, addr = serversocket.accept()
        receive = clientsocket.recv(1024).decode('utf-8')
        
        local_path = "image/"+str(idx)+".jpg"
        urllib.request.urlretrieve(receive, local_path)

        image_path1  = local_path
        im1 = caffe.io.load_image(image_path1)

        prediction1 = net.predict([im1], oversample=False)


        result = 'neutral\n'
        if prediction1[0].argmax() == 1:
            result = 'positive\n'
        elif prediction1[0].argmax() == 0:
            result = 'negative\n'

        idx +=1
        clientsocket.send(result.encode('utf-8'))
        clientsocket.close()
finally:
    clientsocket.close()