import demjson as demjson
from flask import Flask, render_template, request, jsonify, Response
from werkzeug.utils import secure_filename
from datetime import timedelta
import os
import cv2

import datetime
import random

from decryption import decrypt
from encryption import encrypt

app = Flask(__name__)
UPLOAD_FOLDER = 'static\\images'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER


# 输出
@app.route('/')
def hello_world():
    return 'Hello World!'


# 设置允许的文件格式
ALLOWED_EXTENSIONS = set(['png', 'PNG', 'txt'])


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS


# 设置静态文件缓存过期时间
app.send_file_max_age_default = timedelta(seconds=1)


class Name:
    def pic_id(self):
        time_now = datetime.datetime.now().strftime("%Y%m%d%H%M%S");
        rann = random.randint(0, 100);
        if rann <= 10:
            rann = str(0) + str(rann);
        pid = str(time_now) + str(rann);
        return pid;


# 添加路由
@app.route('/send', methods=['POST', 'GET'])
def upload():
    if request.method == 'POST':
        # 通过file标签获取文件
        res = {}
        f1 = request.files['file1']
        if not (f1 and allowed_file(f1.filename)):
            res['code'] = 1
            res['msg'] = "fail"
            return "图片类型错误 应为图片类型：png、PNG"
        # 当前文件所在路径
        basepath = os.path.dirname(__file__)
        # 一定要先创建该文件夹，不然会提示没有该路径
        upload_path1 = os.path.join(basepath, 'static/images', secure_filename(f1.filename))
        # 保存文件
        f1.save(upload_path1)
        # 返回上传成功界面

        f2 = request.files['file2']
        if not (f2 and allowed_file(f2.filename)):
            res['code'] = 1
            res['msg'] = "fail"
            return "图片类型：png、PNG、jpg、JPG、bmp"
        basepath = os.path.dirname(__file__)
        upload_path2 = os.path.join(basepath, 'static/images', secure_filename(f2.filename))
        f2.save(upload_path2)
        upload_path3 = os.path.join(basepath, 'static/images', 'expected_watermark.png')
        upload_path4 = os.path.join(basepath, 'static/images', 'watermarked_image.png')
        upload_path5 = os.path.join(basepath, 'static/images', 'm.txt')
        upload_path6 = os.path.join(basepath, 'static/images', 'n.txt')
        encrypt(upload_path1, upload_path2, upload_path5, upload_path6, upload_path4, upload_path3)

        res['code'] = 0
        res['msg'] = "success"
        return render_template('ok.html')
    # 重新返回上传界面
    return render_template('test.html')


@app.route('/extract', methods=['POST', 'GET'])
def extract():
    if request.method == 'POST':
        # 通过file标签获取文件
        res = {}

        basepath = os.path.dirname(__file__)

        f2 = request.files['file2']
        if not (f2 and allowed_file(f2.filename)):
            res['code'] = 1
            res['msg'] = "fail"
            return "图片类型：png、PNG"
        upload_path2 = os.path.join(basepath, 'static/images', secure_filename(f2.filename))
        f2.save(upload_path2)
        upload_path1 = os.path.join(basepath,'static/images','nanami.png')
        upload_path3 = os.path.join(basepath, 'static/images', 'm.txt')
        upload_path4 = os.path.join(basepath, 'static/images', 'n.txt')

        # 返回上传成功界面
        upload_path5 = os.path.join(basepath, 'static/images', 'extracted_watermark.png')
        decrypt(upload_path1,upload_path2,upload_path3,upload_path4, upload_path5)

        res['code'] = 0
        res['msg'] = "success"
        return render_template('ok.html')
    # 重新返回上传界面
    return render_template('extract.html')


@app.route("/photo/<imgid>.png")
def get_p(imgid):
    with open(r'/home/tmpfll/static/images/{}.png'.format(imgid), 'rb') as f:
        image = f.read()
        resp = Response(image, mimetype="image/png")
        return resp


@app.route("/photo/<txtid>.txt")
def get_frame1(txtid):
    with open(r'/home/tmpfll/static/images/{}.txt'.format(txtid), 'r')as f:
        txt = f.readlines()
        json = demjson.encode(txt)
        res = {}
        res['code'] = 0
        res['data'] = txt
        print(res)
        return res


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5003)
