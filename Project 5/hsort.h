#include <iostream>
#include <vector>
template <typename T, typename Comparator >
void hsort(std::vector<T>& data, Comparator c = Comparator()){
	int i=data.size()-1;
	i=(i-1)/2;
	while(i>=0){
		heapify(data, i, data.size(), c);
		i--;
	}
	//for(int i=0; i<data.size(); i++)std::cout<<*(data[i])<<std::endl;
	int k=data.size();
	int j=data.size()-1;
	for(int i=0; i<k; i++){
		T temp=data[0];
		data[0]=data[j];
		data[j]=temp;
		heapify(data, 0, j, c);
		//for(int i=0; i<data.size(); i++)std::cout<<*(data[i])<<std::endl;
		j--;
	}
}

template <typename T, typename Comparator >
void heapify(std::vector<T>& data, size_t loc, size_t effsize, Comparator& c ){
	if(2*loc+1>=effsize&&2*loc+2>=effsize)return;
	int wanted_child=2*loc+1;
	if(2*loc+2<effsize){
		int rchild=wanted_child+1;
		if(c(data[wanted_child], data[rchild])) wanted_child=rchild;
	}
	if(c(data[loc], data[wanted_child])){
		T temp=data[loc];
		data[loc]=data[wanted_child];
		data[wanted_child]=temp;
		heapify(data, wanted_child, effsize, c);
	}
}
