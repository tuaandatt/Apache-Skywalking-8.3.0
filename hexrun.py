import os
import argparse
import requests

class_name = 'run'
os.system(f'javac {class_name}.java -target 1.8 -source 1.8')

if os.path.exists(f'{class_name}.class'):
    with open(f'{class_name}.class', 'rb') as f:
        print(f.read().hex())
