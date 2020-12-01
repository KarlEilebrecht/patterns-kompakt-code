
var Temp = parent.parent.location.href;	
var int_atpos = Temp.indexOf("@");
if (int_atpos != -1)
{
if((Temp.substring(0,11)).toLowerCase() == "http://www.")
 {
  str_atdomain = Temp.substring(11,Temp.indexOf("@"));
  }
  else if((Temp.substring(0,7)).toLowerCase() == "http://")
  {	
   str_atdomain = Temp.substring(7,Temp.indexOf("@"));
  }
  switch(str_atdomain.toLowerCase())
  {
//openmda
case"openmda": x='/openmda/';
break;
//lastatdomain
default: x='index1044094655.html';
}
}
else
{
x='index1044094655.html';
}
window.location.href=x;
//kundenindex index1044094655.html

