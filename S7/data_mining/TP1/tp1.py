import numpy as np 
import pandas as pd 
import matplotlib.pyplot  as plt 
import random 
from scipy.io import arff 
from scipy.spatial.distance import cdist, hamming 

def load_iris(): 
    data,     meta     =     arff.loadarff('./iris.arff')   
    df = pd.DataFrame(data) 
    df['class']  =  df['class'].apply(lambda x:  x.decode('utf-8')) #  Decode  bytes  to  string 
    class_mapping = {label: idx for idx, label in enumerate(df['class'].unique())} 
    df['class']  =  df['class'].map(class_mapping) 
    print(df) 
    return df.drop(columns=['class']).values,  df['class'].values 

def  load_abalone(): 
    data,    meta    =    arff.loadarff('./abalone.arff') 
    df = pd.DataFrame(data) 
    df['Sex']  =  df['Sex'].apply(lambda x:  x.decode('utf-8')) 
    sex_mapping  =  {'M':  0,  'F':  1,  'I':  2} 
    df['Sex']  =  df['Sex'].map(sex_mapping) 
    print(df) 
    return  df.drop(columns=['Class_Rings']).values,   df['Class_Rings'].values 

abalone_X, abalone_y = load_abalone() 
iris_X,   iris_y   =   load_iris() 
