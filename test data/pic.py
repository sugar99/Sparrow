import requests

def download_image(pic_url):
    md5 = pic_url[pic_url.rfind("/")+1:]
    pic_url += '?p=0'
    r = requests.get(pic_url)
    # 图片格式 -> (r.headers)['Content-Type']
    # 图片数据 -> r.content
    
    Content_Type = (r.headers)['Content-Type']
    if not Content_Type.startswith('image'):
        raise Exception('非图片链接：' + pic_url)

    image_type = Content_Type[Content_Type.find("/")+1:]
    
    # 1.debug: 存储到本地
    filename = md5 + "." + image_type
    with open(filename, 'wb') as image_file:
        image_file.write(r.content)

    # 2.直接存储到oss
    # 使用 Content_Type, image_type, r.content
    
if __name__ == "__main__":
    download_image('http://116.56.140.131:4869/89473df3082687d8bf4f2e2543810534')