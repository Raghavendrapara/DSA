#include <iostream>  // for cout, cin
#include <stack>     // for stack
#include <algorithm> // for min, max
#include <utility>   // for pair
using namespace std;
template <typename T>
class StackWithMin {
private:
    stack< pair<T, T> > S;
public:
    void push(T& x) {
        S.push(pair<T, T>(x, S.empty() ? x : min(x, S.top().second)));
    }

    T pop() {
        if (S.empty())
            throw "stack empty";
        pair<T, T> top = S.top(); S.pop();
        return top.first;
    }

    T getMin() {
        if (S.empty())
            throw "stack empty";
        return S.top().second;
    }

    int size() {
        return S.size();
    }

    bool empty() {
        return S.empty();
    }
};

int main(){

    StackWithMin<const int> obj;
    obj.push(2);
    obj.push(5);
    obj.push(4);
    obj.push(1);
    cout<<obj.getMin()<<endl;
    cout<<obj.size()<<endl;
    return 0;
}
