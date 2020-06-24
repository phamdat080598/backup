
public class Demo {
   
    public static void main(String[] args) {
		int[] list = {1,10,3,6,4,11,11};
		
		quickSort(list,0,6);
		printList(list,7);
    }

    public static void bubbleSort(int[] list,int n){
        for(int i=0;i<n-1;i++){
            for(int j=i+1;j<n;j++){
                if(list[i]>list[j]){
                    swap(i,j,list);
                }
            }
        }
		printList(list,n);
    }

    public static void printList(int[] list,int n){
        for(int i=0;i<n;i++){
            System.out.println(""+list[i]);
        }
    }

    public static void swap(int a,int b,int[] list){
        int temp = list[a];
        list[a]=list[b];
        list[b]=temp;
    }

    public static void selectionSort(int[] list,int n){
        for(int i=0;i<n-1;i++){
            int min=list[i];
            int index = i;
            for(int j=i+1;j<n;j++){
                if(min>list[j]){
                    min = list[j];
                    index=j;
                }
            }
            swap(i,index,list);
        }
		printList(list,n);
    }
	
	public static void quickSort(int[] list,int left,int right){
		int mid = list[(left+right)/2];
		int i=left,j=right;
		do{
			while(list[i]<mid) i++;
			while(list[j]>mid) j--;
			
			if(i<=j){
				swap(i,j,list);
				i++;
				j--;
			}
		}while(i<j);
		
		if(left<j){
			quickSort(list,left,j);
		}
		if(right>i){
			quickSort(list,i,right);
		}
	}

}
